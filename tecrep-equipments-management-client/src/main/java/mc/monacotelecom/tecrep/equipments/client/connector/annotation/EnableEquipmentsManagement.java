package mc.monacotelecom.tecrep.equipments.client.connector.annotation;

import mc.monacotelecom.tecrep.equipments.client.connector.config.EquipmentsManagementClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EquipmentsManagementClientConfiguration.class)
public @interface EnableEquipmentsManagement {
}
