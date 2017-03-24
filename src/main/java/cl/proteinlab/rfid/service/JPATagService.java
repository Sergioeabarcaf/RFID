package cl.proteinlab.rfid.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import cl.proteinlab.rfid.domain.RfidTagLecture;
import cl.proteinlab.rfid.repository.RfidTagLectureRepository;

/**
 * @author Patricio A. Pérez Valverde
 * @since 21-03-17
 */
@Service
public class JPATagService implements TagService {

    @Autowired
    private RfidTagLectureRepository repository;

    @Transactional
    @Override
    public RfidTagLecture guardar(RfidTagLecture tag) {
        RfidTagLecture save = null;
        try {
            if (tag != null && StringUtils.isNotEmpty(tag.getEpc()) && tag.getFecha() != null) {
                save = repository.save(tag);
            }
        } catch (Exception e) {
            save = null;
            e.printStackTrace();
        }
        return save;
    }

    @Override
    public List<RfidTagLecture> consultarTodas() {
        List<RfidTagLecture> lecturas = new ArrayList<>();
        try {
            lecturas = repository.findAll();
        } catch (Exception e) {
            lecturas = new ArrayList<>();
            e.printStackTrace();
        }
        return lecturas;
    }

}
