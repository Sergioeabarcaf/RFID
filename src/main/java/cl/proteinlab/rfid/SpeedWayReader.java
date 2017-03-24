package cl.proteinlab.rfid;

import com.impinj.octane.ImpinjReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Patricio A. Pérez Valverde
 * @since 24-03-17
 */
@Configuration
public class SpeedWayReader {

    @Bean
    public ImpinjReader reader() {
        return new ImpinjReader();
    }
}
