package cl.proteinlab.rfid.listeners;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import cl.proteinlab.rfid.domain.RfidTagLecture;
import cl.proteinlab.rfid.service.TagService;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
@Component
@Qualifier("tagLectureListener")
public class TagLectureReportListener implements TagReportListener {

    @Autowired
    private TagService tagService;

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            RfidTagLecture tag = new RfidTagLecture();
            tag.setEpc(t.getEpc().toString());
            RfidTagLecture guardar = tagService.guardar(tag);
            if (guardar != null) {
                System.out.println(String.format("Se guardo el tag: %s", tag));
            } else {
                System.out.println(String.format("Hubo un problema guardando el tag: %s", t.toString()));
            }
        }
    }
}
