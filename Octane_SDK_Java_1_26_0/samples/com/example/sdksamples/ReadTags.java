package com.example.sdksamples;

import com.impinj.octane.*;

import java.util.Scanner;

public class ReadTags {

    public static void main(String[] args) {

        try {
            String hostname = System.getProperty(SampleProperties.hostname);

            if (hostname == null) {
                throw new Exception("Must specify the '"
                        + SampleProperties.hostname + "' property");
            }

            /*Crear e inicializar el objeto ImpinjReader, Tambien se puede 
            *inicializar con ImpinjReader(java.lang.String address, java.lang.String name)*/
            ImpinjReader reader = new ImpinjReader();

            System.out.println("Connecting");
            /*Conectar de manera no segura al lector Impinj usando por defecto 
            el puerto LLRP 5048, entrega por parametro la direccion IP*/
            reader.connect(hostname);

            /* Consulta los ajustes predeterminados del lector. NOTA los ajustes por defecto 
            del lector pueden ser diferentes de los ajustes por defecto del SDK. Para usar 
            los ajustes predeterminados del SDK, asegúrese de aplicar esta configuración*/
            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setIncludePeakRssi(true);
            report.setIncludeDopplerFrequency(true);
            report.setIncludeChannel(true);
            report.setIncludeSeenCount(true);
            report.setMode(ReportMode.Individual);

            /* Configuracion modo de lectura. (AutoSetDenseReader,DenseReaderM4, DenseReaderM8)*/
            settings.setReaderMode(ReaderMode.DenseReaderM8);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            /*Configuracion antena 1*/
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            /*Configuracion de potencia y sensibilidad*/
            antennas.getAntenna((short) 1).setTxPowerinDbm(24.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-80);
            /*Configuracion antena 2*/
 /*
            antennas.enableById(new short[]{2});
            antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 2).setIsMaxTxPower(false);
            antennas.getAntenna((short) 2).setTxPowerinDbm(15.0);
            antennas.getAntenna((short) 2).setRxSensitivityinDbm(-40);
             */


 /*
            Establece el receptor de informes de variables. Este listener 
            permite que la aplicación sea notificada cuando el informe de 
            etiqueta llegue desde el lector.*/
            reader.setTagReportListener(new TagReportListenerImplementation());

            System.out.println("Applying Settings");
            /*
            Aplica la configuración proporcionada al lector, por defecto*/
            reader.applySettings(settings);

            System.out.println("Starting");
            /* Inicia el lector. Los informes de etiquetas se recibirán de forma asíncrona a través de un evento.*/
            reader.start();
            

            System.out.println("Press Enter to exit.");
            Scanner s = new Scanner(System.in);
            s.nextLine();

            /* Detiene el lector de su inventario. Las etiquetas no se leerán más.*/
            reader.stop();
            /* Desconecta la conexión LLRP al lector */
            reader.disconnect();

        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
