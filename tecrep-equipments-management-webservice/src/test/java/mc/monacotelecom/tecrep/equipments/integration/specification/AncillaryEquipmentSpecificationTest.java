package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static mc.monacotelecom.tecrep.equipments.enums.EquipmentName.HDD;
import static mc.monacotelecom.tecrep.equipments.enums.EquipmentNature.ADDITIONAL;
import static mc.monacotelecom.tecrep.equipments.enums.Status.ASSIGNED;
import static mc.monacotelecom.tecrep.equipments.enums.Status.DEACTIVATED;
import static mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.AbstractAncillarySearcher.prepareSpecification;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional(readOnly = true)
@Sql({"/sql/clean.sql", "/sql/ancillaryEq_data.sql"})
class AncillaryEquipmentSpecificationTest extends BaseIntegrationTest {

    @Nested
    class WithSN {
        @Test
        void search_withSerialNumber_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setSerialNumber("GFAB02600018");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 2L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withSerialNumber_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setSerialNumber("GFAB02609999");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withSerialNumber_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setSerialNumber("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithBatchNumber {
        @Test
        void search_withBatchNumber_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setBatchNumber("B01");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withBatchNumber_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setBatchNumber("NOTFOUND");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withBatchNumber_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setBatchNumber("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithIndependent {
        @Test
        void search_isIndependent_true() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setIndependent(true);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(3, result.size());
        }

        @Test
        void search_isIndependent_false() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setIndependent(false);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(2, result.size());
        }

        @Test
        void search_isIndependent_null() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setIndependent(null);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithMacAddress {
        @Test
        void search_withMacAddress_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setMacAddress("00:0A:95:9D:68:17");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 4L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withMacAddress_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setMacAddress("00:0A:95:9D:99:99");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withMacAddress_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setMacAddress("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithModel {
        @Test
        void search_withModelName_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setModelName("test_4");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 5L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withModelName_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setModelName("test_99");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withModelName_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setModelName("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithEquipmentName {
        @Test
        void search_withEquipmentName_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setEquipmentName(HDD);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 4L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withEquipmentName_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setEquipmentName(EquipmentName.STB);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithPairedEquipment {
        @Test
        void search_hasEquipment_true() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setEquipment(true);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_hasEquipment_false() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setEquipment(false);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(4, result.size());
        }

        @Test
        void search_hasEquipment_null() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setEquipment(null);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }


        @Test
        void search_withPairedEquipmentId_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setPairedEquipmentId(1L);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withPairedEquipmentId_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setPairedEquipmentId(99L);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithStatus {
        @Test
        void search_withStatus_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setStatus(ASSIGNED);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 3L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withStatus_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setStatus(DEACTIVATED);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithNature {
        @Test
        void search_withNature_additional() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setNature(ADDITIONAL);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 5L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withNature_main() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setNature(EquipmentNature.MAIN);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(4, result.size());
        }
    }

    @Nested
    class WithProvider {
        @Test
        void search_withProviderName_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setProvider("Tata");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 5L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withProviderName_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setProvider("MISSING");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withProviderName_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setProvider("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithWarehouse {
        @Test
        void search_withWarehouseName_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setWarehouse("TATA");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(2, result.size());
        }

        @Test
        void search_withWarehouseName_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setWarehouse("MISSING");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withWarehouseName_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setWarehouse("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithAccessType {
        @Test
        void search_withAccessType_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setAccessType(AccessType.FTTH);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 4L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withAccessType_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setAccessType(AccessType.FTTH);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }
    }

    @Nested
    class WithOrder {
        @Test
        void search_withOrderId_found() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrderId("2");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(1, result.size());
            final var AncillaryEquipmentDTOV2 = result.stream().findFirst().get();
            assertEquals((Long) 4L, AncillaryEquipmentDTOV2.getId());
        }

        @Test
        void search_withOrderId_notFound() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrderId("MISSING");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(0, result.size());
        }

        @Test
        void search_withOrderId_notValued() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrderId("");

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }

        @Test
        void search_withOrder_true() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrder(true);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(3, result.size());
        }

        @Test
        void search_withOrder_false() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrder(false);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(2, result.size());
        }

        @Test
        void search_withOrder_null() {
            SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryEquipmentDTO.setOrder(null);

            Collection<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable).getContent();

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithExternalNumber {
        @Test
        void search_externalNumber_found() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setExternalNumber("37799900002");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));
            final var dto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 4L, dto.getEquipmentId())
            );
        }

        @Test
        void search_externalNumber_notFound() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setExternalNumber("37799900001");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_externalNumber_notValued() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setExternalNumber("");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(5, result.size());
        }
    }

    @Nested
    class WithSfpVersion {
        @Test
        void search_sfpVersion_found() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setSfpVersion("PO2020071700001");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));
            final var dto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 5L, dto.getEquipmentId())
            );
        }

        @Test
        void search_sfpVersion_notFound() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setSfpVersion("PO2020071700002");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_sfpVersion_notValued() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setExternalNumber("");
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(5, result.size());
        }
    }


    @Nested
    class WithService {
        @Test
        void search_serviceId_found() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setServiceId(5L);
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));
            final var dto = result.stream().findFirst().get();

            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals((Long) 3L, dto.getEquipmentId())
            );
        }

        @Test
        void search_serviceId_notFound() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setServiceId(22L);
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(0, result.size());
        }

        @Test
        void search_serviceId_notValued() {
            var searchAncillaryExporterDTO = new SearchAncillaryEquipmentDTO();
            searchAncillaryExporterDTO.setServiceId(null);
            var result = ancillaryRepository.findAll(prepareSpecification(searchAncillaryExporterDTO));

            assertEquals(5, result.size());
        }
    }
}
