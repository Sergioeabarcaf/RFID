package cl.proteinlab.rfid.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
public class BaseBean implements Serializable {
    private static final long serialVersionUID = -3767988053460362034L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
