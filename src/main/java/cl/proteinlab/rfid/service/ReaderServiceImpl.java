package cl.proteinlab.rfid.service;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.SearchMode;
import com.impinj.octane.Settings;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagReportListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Patricio A. Pérez Valverde
 * @since 23-03-17
 */
@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ImpinjReader reader;

    @Value("${reader.hostname}")
    private String hostname;

    @Autowired
    @Qualifier("tagLectureListener")
    private TagReportListener tagLectureReportListener;

    @Autowired
    @Qualifier("tagWriterListener")
    private TagReportListener tagWriterReportListener;

    @Autowired
    @Qualifier("tagWriterListener")
    private TagOpCompleteListener tagWriteOpCompleteListener;

    @Override
    public ImpinjReader startReader() {
        try {
            System.out.println("Connecting [mode: reader to mysql]");
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setIncludePeakRssi(true);
            report.setMode(ReportMode.Individual);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader’s configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(28.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            antennas.enableById(new short[]{2});
            antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 2).setIsMaxTxPower(false);
            antennas.getAntenna((short) 2).setTxPowerinDbm(28.0);
            antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

            reader.setTagReportListener(tagLectureReportListener);

            System.out.println("Applying Settings");
            reader.applySettings(settings);
            reader.start();
        } catch (OctaneSdkException e) {
            reader = new ImpinjReader();
            e.printStackTrace();
        }
        return reader;
    }

    @Override
    public ImpinjReader stopReader() {
        try {
            reader.stop();
            reader.disconnect();
        } catch (OctaneSdkException e) {
            e.printStackTrace();
        }
        return reader;
    }

    @Override
    public ImpinjReader startWriter() {
        try {
            // Connect
            System.out.println("Connecting to " + hostname);
            reader.connect(hostname);

            // Get the default settings
            Settings settings = reader.queryDefaultSettings();

            // just use a single antenna here
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(28.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            antennas.enableById(new short[]{2});
            antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 2).setIsMaxTxPower(false);
            antennas.getAntenna((short) 2).setTxPowerinDbm(28.0);
            antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

            // set session one so we see the tag only once every few seconds
            settings.getReport().setIncludeAntennaPortNumber(true);
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);
            settings.setSearchMode(SearchMode.SingleTarget);
            settings.setSession(1);
            // turn these on so we have them always
            settings.getReport().setIncludePcBits(true);

            // Set periodic mode so we reset the tag and it shows up with its
            // new EPC
            settings.getAutoStart().setMode(AutoStartMode.Periodic);
            settings.getAutoStart().setPeriodInMs(2000);
            settings.getAutoStop().setMode(AutoStopMode.Duration);
            settings.getAutoStop().setDurationInMs(1000);

            // Apply the new settings
            reader.applySettings(settings);

            // set up listeners to hear stuff back from SDK
            reader.setTagReportListener(tagWriterReportListener);
            reader.setTagOpCompleteListener(tagWriteOpCompleteListener);
            reader.start();
        } catch (OctaneSdkException e) {
            e.printStackTrace();
        }
        return reader;
    }

}
