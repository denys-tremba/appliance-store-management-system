package com.example.rd.autocode.assessment.appliances.appliance.manageAppliance;

import com.example.rd.autocode.assessment.appliances.appliance.Appliance;
import com.example.rd.autocode.assessment.appliances.appliance.ApplianceRepository;
import com.example.rd.autocode.assessment.appliances.appliance.find.ApplianceNotFound;
import com.example.rd.autocode.assessment.appliances.appliance.manage.CreateApplianceParameters;
import com.example.rd.autocode.assessment.appliances.appliance.manage.EditApplianceParameters;
import com.example.rd.autocode.assessment.appliances.appliance.manage.ManageApplianceService;
import com.example.rd.autocode.assessment.appliances.auxiliary.ApplianceBuilder;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerNotFound;
import com.example.rd.autocode.assessment.appliances.manufacturer.manage.ManufacturerRepository;
import com.example.rd.autocode.assessment.appliances.order.OrderRepository;
import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManageApplianceServiceTest {
    ManufacturerRepository manufacturerRepository = Mockito.mock(ManufacturerRepository.class);
    ApplianceRepository applianceRepository = Mockito.mock(ApplianceRepository.class);
    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    VectorStore vectorStore = Mockito.mock(VectorStore.class);
    ManageApplianceService service = new ManageApplianceService(manufacturerRepository, applianceRepository, orderRepository, vectorStore);
    Manufacturer existingManufacturer = new Manufacturer(1L, "Test Manufacturer");
    Appliance existingAppliance = new ApplianceBuilder().id(10L).build();
    @Test
    void createShouldSaveApplianceWhenManufacturerExists() {
        CreateApplianceParameters params = CreateApplianceParameters
                .builder()
                .manufacturerId(1L)
                .build();

        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(existingManufacturer));
        when(applianceRepository.save(any(Appliance.class))).thenReturn(existingAppliance);

        service.create(params);

        verify(applianceRepository, times(1)).save(any(Appliance.class));
    }

    @Test
    void createShouldThrowManufacturerNotFoundWhenManufacturerDoesNotExist() {
        CreateApplianceParameters params = CreateApplianceParameters
                .builder()
                .manufacturerId(99L)
                .build();

        when(manufacturerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ManufacturerNotFound.class, () -> service.create(params));

        verify(applianceRepository, never()).save(any());
    }


    @Test
    void editShouldUpdateApplianceWhenDataIsValid() {
        EditApplianceParameters params = EditApplianceParameters
                .builder()
                .manufacturer(1L)
                .version(0)
                .build();

        when(applianceRepository.findById(10L)).thenReturn(Optional.of(existingAppliance));
        when(orderRepository.checkApplianceUsageInLineItems(10L)).thenReturn(false); // Not used in any order
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(existingManufacturer));

        Appliance updatedAppliance = service.edit(10L, params);

        assertThat(updatedAppliance).isNotNull();
    }

    @Test
    void editShouldThrowApplianceNotFoundWhenApplianceDoesNotExist() {
        when(applianceRepository.findById(anyLong())).thenReturn(Optional.empty());
        EditApplianceParameters params = EditApplianceParameters.builder().build();

        assertThrows(ApplianceNotFound.class, () -> service.edit(99L, params));
    }

    @Test
    void editShouldThrowOrderExceptionWhenApplianceIsInUse() {
        when(applianceRepository.findById(10L)).thenReturn(Optional.of(existingAppliance));
        when(orderRepository.checkApplianceUsageInLineItems(10L)).thenReturn(true); // Appliance is in an order
        EditApplianceParameters params = EditApplianceParameters.builder().build();

        assertThrows(ManufacturerNotFound.class, () -> service.edit(10L, params));
    }

    @Test
    void editShouldThrowOptimisticLockingExceptionWhenVersionsMismatch() {
        EditApplianceParameters params = EditApplianceParameters
                .builder()
                .manufacturer(1L)
                .version(0)
                .build();

        existingAppliance.setVersion(1);

        when(applianceRepository.findById(10L)).thenReturn(Optional.of(existingAppliance));
        when(orderRepository.checkApplianceUsageInLineItems(10L)).thenReturn(false);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(existingManufacturer));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.edit(10L, params));
    }


    @Test
    void deleteShouldCallDeleteByIdWhenApplianceIsNotInUse() {
        when(orderRepository.checkApplianceUsageInLineItems(10L)).thenReturn(false);

        service.delete(10L);

        verify(applianceRepository, times(1)).deleteById(10L);
    }

    @Test
    void deleteShouldThrowOrderExceptionWhenApplianceIsInUse() {
        when(orderRepository.checkApplianceUsageInLineItems(10L)).thenReturn(true);

        assertThrows(OrderException.class, () -> service.delete(10L));

        verify(applianceRepository, never()).deleteById(anyLong());
    }
}