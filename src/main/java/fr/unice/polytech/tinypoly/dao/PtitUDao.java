package fr.unice.polytech.tinypoly.dao;

import fr.unice.polytech.tinypoly.entities.Account;
import fr.unice.polytech.tinypoly.entities.PtitU;

import java.util.List;

public interface PtitUDao {

    long createPtitU(PtitU ptitU);

    PtitU readPtitU(long hash);

    List<PtitU> listPtitUs(String startCursorString);

}
