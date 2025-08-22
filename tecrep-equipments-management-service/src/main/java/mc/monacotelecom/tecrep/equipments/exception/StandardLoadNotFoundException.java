package mc.monacotelecom.tecrep.equipments.exception;

public class StandardLoadNotFoundException extends RuntimeException {
    private final Long idStandard;

    public StandardLoadNotFoundException(Long idStandard) {
        super("standard_load id not found");
        this.idStandard = idStandard;
    }

    public Long getIdStandard() {
        return idStandard;
    }
}
