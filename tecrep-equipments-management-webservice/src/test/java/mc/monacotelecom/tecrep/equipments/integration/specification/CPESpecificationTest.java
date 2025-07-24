package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static mc.monacotelecom.tecrep.equipments.process.cpe.CPESearcher.prepareSpecification;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional(readOnly = true)
@Sql({"/sql/clean.sql", "/sql/cpe_data.sql"})
class CPESpecificationTest extends BaseIntegrationTest {

    @Nested
    class WithSN {

        @Test
        void search_withSerialNumber_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setSerialNumber("8937710330000000008");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withSerialNumber_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setSerialNumber("8937710330099999999");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withSerialNumber_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setSerialNumber("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddressCPE {
        @Test
        void search_withMacAddressCPE_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressCpe("00:0A:95:9D:68:15");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withMacAddressCPE_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressCpe("00:0A:95:9D:68:99");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withMacAddressCPE_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressCpe("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddressRouter {
        @Test
        void search_withMacAddressRouter_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressRouter("00:0A:95:9D:68:21");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withMacAddressRouter_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressRouter("00:0A:95:9D:68:99");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withMacAddressRouter_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressRouter("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddressVoip {
        @Test
        void search_withMacAddressVoip_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressVoip("00:0A:95:9D:68:35");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withMacAddressVoip_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressVoip("00:0A:95:9D:68:99");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withMacAddressVoip_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressVoip("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithModel {
        @Test
        void search_withModelName_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setModelName("Toto");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withModelName_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setModelName("MISSING");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withModelName_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setModelName("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithStatus {
        @Test
        void search_withStatus_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setStatus(Status.AVAILABLE);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withStatus_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setStatus(Status.INSTORE);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithNature {
        @Test
        void search_withNature_main() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setNature(EquipmentNature.MAIN);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(3, result.size());
        }

        @Test
        void search_withNature_additional() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setNature(EquipmentNature.ADDITIONAL);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(2, result.size());
        }
    }

    @Nested
    class WithProvider {
        @Test
        void search_withProviderName_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setProvider("Tata");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withProviderName_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setProvider("MISSING");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withProviderName_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setProvider("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithWarehouse {
        @Test
        void search_withWarehouseName_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setWarehouse("TATA");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 1L, CPEDTOV2.getId());
        }

        @Test
        void search_withWarehouseName_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setWarehouse("MISSING");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withWarehouseName_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setWarehouse("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithAccessType {
        @Test
        void search_withAccessType_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setAccessType(AccessType.FTTH);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, CPEDTOV2.getId());
        }

        @Test
        void search_withAccessType_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setAccessType(AccessType.STB_STICKTV);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithExternalNumber {
        @Test
        void search_withExternalNumber_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setExternalNumber("37799900001");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, CPEDTOV2.getId());
        }

        @Test
        void search_withExternalNumber_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setExternalNumber("MISSING");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withExternalNumber_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setExternalNumber("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithBatch {
        @ParameterizedTest
        @CsvSource({"1,3", "123,0", ",5"})
        void search_withBatchNumber(String input, int expected) {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setBatchNumber(input);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(expected, result.size());
        }
    }

    @Nested
    class WithOrder {
        @Test
        void search_withOrderId_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrderId("2");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, CPEDTOV2.getId());
        }

        @Test
        void search_withOrderId_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrderId("123");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withOrderId_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrderId("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }

        @Test
        void search_hasOrder_true() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrder(true);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(4, result.size());
        }

        @Test
        void search_hasOrder_false() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrder(false);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
        }

        @Test
        void search_hasOrder_null() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setOrder(null);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithService {
        @Test
        void search_withServiceId_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setServiceId("2");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, CPEDTOV2.getId());
        }

        @Test
        void search_withServiceId_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setServiceId("123");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withServiceId_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setServiceId("");

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }

        @Test
        void search_hasService_true() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setService(true);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(1, result.size());
            final var CPEDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, CPEDTOV2.getId());
        }

        @Test
        void search_hasService_false() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setService(false);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(4, result.size());
        }

        @Test
        void search_hasService_null() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setService(null);

            Collection<CPEDTOV2> result = cpeSearcher.search(searchCpeDto, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddress {

        String macAddress;

        @Test
        void search_macAddressCpe_found() {
            macAddress = "00:0A:95:9D:68:14";
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddress(macAddress);
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 1L, cpeDto.getEquipmentId()),
                    () -> assertEquals(macAddress, cpeDto.getMacAddressCpe())
            );
        }

        @Test
        void search_macAddressRouter_found() {
            macAddress = "00:0A:95:9D:68:21";
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddress("00:0A:95:9D:68:21");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 2L, cpeDto.getEquipmentId()),
                    () -> assertEquals(macAddress, cpeDto.getMacAddressRouter())
            );
        }

        @Test
        void search_macAddressVoid_found() {
            macAddress = "00:0A:95:9D:68:35";
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddress(macAddress);
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 2L, cpeDto.getEquipmentId()),
                    () -> assertEquals(macAddress, cpeDto.getMacAddressVoip())
            );
        }

        @Test
        void search_macAddress_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddress("00:0A:95:9D:68:99");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(0, result.size());
        }

        @Test
        void search_macAddress_blank() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddress("");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddressLan {
        @Test
        void search_macAddressLan_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddressLan("00:0A:95:9D:68:20");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 1L, cpeDto.getEquipmentId())
            );
        }

        @Test
        void search_macAddressLan_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddressLan("00:0A:95:9D:68:99");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(0, result.size());
        }

        @Test
        void search_macAddressLan_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddressLan("");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddress5G {
        @Test
        void search_macAddress5G_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddress5G("00:0A:95:9D:68:71");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 2L, cpeDto.getEquipmentId())
            );
        }

        @Test
        void search_macAddress5G_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddress5G("00:0A:95:9D:68:99");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(0, result.size());
        }

        @Test
        void search_macAddress5G_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddressLan("");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddress4G {
        @Test
        void search_macAddress4G_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setMacAddress4G("00:0A:95:9D:68:46");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 2L, cpeDto.getEquipmentId())
            );
        }

        @Test
        void search_macAddress4G_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddress4G("00:0A:95:9D:68:99");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(0, result.size());
        }

        @Test
        void search_macAddress4G_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddressLan("");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithHwVersion {
        @Test
        void search_withHwVersion_found() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();
            searchCpeDto.setHwVersion("1.0.5");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));
            final var cpeDto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 2L, cpeDto.getEquipmentId())
            );
        }

        @Test
        void search_withHwVersion_notFound() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setHwVersion("A4");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(0, result.size());
        }

        @Test
        void search_withHwVersion_notValued() {
            SearchCpeDto searchCpeDto = new SearchCpeDto();

            searchCpeDto.setMacAddressLan("");
            var result = cpeRepository.findAll(prepareSpecification(searchCpeDto));

            assertEquals(5, result.size());
        }
    }
}
