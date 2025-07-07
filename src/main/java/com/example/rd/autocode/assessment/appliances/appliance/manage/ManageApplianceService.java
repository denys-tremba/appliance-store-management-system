package com.example.rd.autocode.assessment.appliances.appliance.manage;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerNotFound;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerRepository;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('EMPLOYEE')")
public class ManageApplianceService {
    ManufacturerRepository manufacturerRepo;
    ApplianceRepository applianceRepository;
    OrderRepository orderRepository;
    VectorStore vectorStore;

    @Transactional
    public Appliance create(CreateApplianceParameters parameters) {
        Manufacturer manufacturer = manufacturerRepo.findById(parameters.getManufacturerId()).orElseThrow(ManufacturerNotFound::new);
        Appliance appliance = new Appliance(null, parameters.getName(), parameters.getCategory(), parameters.getModel(), parameters.getPowerType(), manufacturer, parameters.getCharacteristic(), parameters.getDescription(), parameters.getPower(), parameters.getPrice(), null, null, 0);
        appliance = applianceRepository.save(appliance);
        vectorStore.add(Collections.singletonList(convertToDoc(appliance)));
        return appliance;
    }

    public Page<Appliance> findAll(Pageable pageable) {
        return applianceRepository.findAll(pageable);
    }

    public Appliance findById(Long id) {
        return applianceRepository.findById(id).orElseThrow(ApplianceNotFound::new);
    }

    @Transactional
    public Appliance edit(Long id, EditApplianceParameters parameters) {
        Appliance appliance = applianceRepository.findById(id).orElseThrow(ApplianceNotFound::new);
        if (orderRepository.checkApplianceUsageInLineItems(id)) {
            throw new OrderException("You are not allowed to edit this appliance because of existing order referencing it");
        }
        Manufacturer manufacturer = manufacturerRepo.findById(parameters.getManufacturer()).orElseThrow(ManufacturerNotFound::new);
        if (parameters.getVersion() != appliance.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(Appliance.class,
                    id,
                    "form version=%s is not aligned with appliance version=%s".formatted(parameters.getVersion(), appliance.getVersion()),
                    null);
        }

        parameters.update(appliance, manufacturer);
        return appliance;
    }

    @Transactional
    public void delete(Long id) {
        if (orderRepository.checkApplianceUsageInLineItems(id)) {
            throw new OrderException("You are not allowed to delete this appliance because of existing order referencing it");
        }
        applianceRepository.deleteById(id);
        vectorStore.delete(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("id"), new Filter.Value(id)));
    }

    public static Document convertToDoc(Appliance appliance) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("model", appliance.getModel());
        metadata.put("name", appliance.getName());
        metadata.put("id", appliance.getId());
        metadata.put("characteristic", appliance.getCharacteristic());
        metadata.put("price", appliance.getPrice());
        metadata.put("category", appliance.getCategory().name());
        metadata.put("manufacturer", appliance.getManufacturer().getName());
        metadata.put("power", appliance.getPower());
        return new Document(appliance.getDescription(), metadata);
    }
}
