package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static mc.monacotelecom.tecrep.equipments.enums.AccessType.FTTH;
import static mc.monacotelecom.tecrep.equipments.process.simcard.searcher.AbstractSimCardSearcher.prepareSpecifications;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional(readOnly = true)
@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
class SimCardSpecificationTests extends BaseIntegrationTest {

    @Nested
    class WithIMSI {
        @Test
        void search_withImsiFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsi("123456789012346");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012346", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withImsiNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsi("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withImsiNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsi("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithSN {
        @Test
        void search_withSnFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setSn("893771033000000008");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012346", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withSnNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setSn("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withSnNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setSn("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithPSN {
        @Test
        void search_withPsnFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPsn("893771033000000010");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withPsnNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPsn("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withPsnNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPsn("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithStatus {
        @Test
        void search_withStatusFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setStatus(Status.DEACTIVATED);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withStatusNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setStatus(Status.ACTIVATED);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithIMSISN {
        @Test
        void search_withImsiSnFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsisn("123456");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withImsiSnNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsisn("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withImsiSnNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setImsisn("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithNature {
        @Test
        void search_withNatureFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setNature(EquipmentNature.ADDITIONAL);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(2, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012348", simCardDTO.getImsiNumber());
        }
    }

    @Nested
    class WithProvider {
        @Test
        void search_withProviderFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setProvider("Tata");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withProviderNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setProvider("XXX");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withProviderNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setProvider("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithWarehouse {
        @Test
        void search_withWarehouseFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setWarehouse("TATA");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012345", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withWarehouseNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setWarehouse("XXX");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withWarehouseNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setWarehouse("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithService {
        @Test
        void search_withServiceFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setService(true);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withServiceIdFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setServiceId("2");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withServiceIdNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setServiceId("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withServiceIdNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setServiceId("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithOrder {
        @Test
        void search_withOrderIdFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setOrderId("2");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(2, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012348", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withOrderIdNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setOrderId("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withOrderIdNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setOrderId("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }

        @Test
        void search_withOrderFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setOrder(true);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(3, result.size());
        }
    }

    @Nested
    class WithAccessType {
        @Test
        void search_withAccessTypeFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setAccessType(FTTH);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(2, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012348", simCardDTO.getImsiNumber());
        }
    }

    @Nested
    class WithExternalNumber {
        @Test
        void search_withExternalNumberFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setExternalNumber("779990000");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withExternalNumberNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setExternalNumber("999999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withExternalNumberNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setExternalNumber("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithPreactivated {
        @Test
        void search_withPreactivatedFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPreactivated(true);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }
    }

    @Nested
    class WithBatchNumber {
        @Test
        void search_withBatchNumberFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBatchNumber("2");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(2, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("893771033000000010", simCardDTO.getSerialNumber());
        }

        @Test
        void search_withBatchNumberNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBatchNumber("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withBatchNumberNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBatchNumber("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithPackId {
        @Test
        void search_withPackIdFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPackId("7");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withPackIdNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPackId("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withPackIdNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPackId("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithInventoryPool {
        @Test
        void search_withInventoryPoolCodeFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolCode("1235");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withInventoryPoolCodeNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolCode("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withInventoryPoolCodeNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolCode("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }

        @Test
        void search_withInventoryPoolIdCodeFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolId("2");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withInventoryPoolIdNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolId("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withInventoryPoolIdNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setInventoryPoolId("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithAllotment {
        @Test
        void search_withAllotmentIdFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setAllotmentId("6");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withAllotmentIdNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setAllotmentId("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withAllotmentIdNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setAllotmentId("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithNumber {
        @Test
        void search_withNumberFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setNumber("456789");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(1, result.size());
            final var simCardDTO = result.stream().findFirst().get();
            assertEquals("123456789012347", simCardDTO.getImsiNumber());
        }

        @Test
        void search_withNumberNotFound() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setNumber("999");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(0, result.size());
        }

        @Test
        void search_withNumberNotValued() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setNumber("");

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithESim {
        @Test
        void search_withEsim() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setEsim(true);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(3, result.size());
        }

        @Test
        void search_withEsim_false() {
            SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setEsim(false);

            Collection<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable).getContent();


            assertEquals(2, result.size());
        }
    }

    @Nested
    class WithPuk {
        @Test
        void search_puk1Code_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk1Code("10015955");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 5L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_puk1Code_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk1Code("10015954");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_puk1Code_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk1Code("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }

        @Test
        void search_puk2Code_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk2Code("10015954");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 5L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_puk2Code_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk2Code("10015957");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_puk2Code_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPuk2Code("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithPin {
        @Test
        void search_pin1Code_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin1Code("1001");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 5L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_pin1Code_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin1Code("1003");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_pin1Code_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin1Code("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }

        @Test
        void search_pin2Code_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin2Code("1002");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 5L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_pin2Code_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin2Code("1001");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_pin2Code_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPin2Code("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithBrand {
        @Test
        void search_brand_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBrand("test");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(5, result.size()),
                    () -> assertEquals((Long) 1L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_brand_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBrand("test_not_found");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_brand_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setBrand("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithPlmn {
        @Test
        void search_plmnCode_found() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPlmnCode("12345");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));
            final var simcard = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(5, result.size()),
                    () -> assertEquals((Long) 1L, simcard.getEquipmentId())
            );
        }

        @Test
        void search_plmnCode_notFound() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPlmnCode("1001");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_plmnCode_notValued() {
            var searchSimCardDTO = new SearchSimCardDTO();
            searchSimCardDTO.setPlmnCode("");
            var result = simCardRepository.findAll(prepareSpecifications(searchSimCardDTO));

            assertEquals(5, result.size());
        }
    }
}
