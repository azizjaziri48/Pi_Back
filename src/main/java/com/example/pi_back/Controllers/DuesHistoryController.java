package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.DuesHistory;
import com.example.pi_back.Services.IDuesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Validated
@RequestMapping("/DuesHistory")
public class DuesHistoryController {


    @Autowired
    IDuesHistoryService DuesHistoryservice;

    // http://localhost:8083/pi_back/DuesHistory/retrieve-all-DuesHistory
    @GetMapping("/retrieve-all-DuesHistory")
    @ResponseBody
    public List<DuesHistory> getDuesHistory() {
        List<DuesHistory> listDuesHistorys = DuesHistoryservice.retrieveAllDuesHistorys();
        return listDuesHistorys;
    }

    // http://localhost:8083/pi_back/DuesHistory/retrieve-all-DuesHistory-Credit/43
    @GetMapping("/retrieve-all-DuesHistory-Credit/{idcredit}")
    @ResponseBody
    public List<DuesHistory> getDuesHistoryByCredit(@PathVariable("idcredit") Long idcredit) {
        List<DuesHistory> listDuesHistorys = DuesHistoryservice.retrieveAllDuesHistory_byCredit(idcredit);
        return listDuesHistorys;
    }


    //http://localhost:8083/pi_back/DuesHistory/retrieve-DuesHistory/1
    @GetMapping("/retrieve-DuesHistory/{DuesHistory-id}")
    @ResponseBody
    public DuesHistory retrieveDuesHistory(@PathVariable("DuesHistory-id") Long DuesHistoryId) {
        return DuesHistoryservice.retrieveDuesHistory(DuesHistoryId);
    }

    //http://localhost:8083/pi_back/DuesHistory/add-DuesHistory/1
    @PostMapping("/add-DuesHistory/{DuesHistory-id_credit}")
    @ResponseBody
    public DuesHistory addDuesHistory(@Valid @RequestBody DuesHistory c,
                                      @PathVariable("DuesHistory-id_credit") Long id_credit)
    {
        DuesHistory DuesHistory = DuesHistoryservice.addDuesHistory(c,id_credit);
        return DuesHistory;
    }

    //http://localhost:8083/pi_back/DuesHistory/modify-DuesHistory/1/1/1
    @PutMapping("/modify-DuesHistory/{DuesHistory-Id_client}")
    @ResponseBody
    public DuesHistory modifyDuesHistory(@RequestBody DuesHistory DuesHistory,@PathVariable("DuesHistory-Id_client") Long Id_client) {
        return DuesHistoryservice.updateDuesHistory(DuesHistory,Id_client);
    }

    //http://localhost:8083/pi_back/DuesHistory/remove-DuesHistory/{DuesHistory-id}
    @DeleteMapping("/remove-DuesHistory/{DuesHistory-id}")
    @ResponseBody
    public void removeDuesHistory(@PathVariable("DuesHistory-id") Long DuesHistoryId) {
        DuesHistoryservice.DeleteDuesHistory(DuesHistoryId);
    }
}
