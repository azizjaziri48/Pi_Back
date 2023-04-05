package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.*;
import com.example.pi_back.Repositories.InternalServiceRepository;
import com.example.pi_back.Services.InternalSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class KmeansController {
    @Autowired
    InternalSService internalService;

    @Autowired
    InternalServiceRepository rep;

    @GetMapping("/kmeansForDataanalysis")
    @ResponseBody
    public KMeans kmeans() {
        // KMeansPlusPlusInitialiser

        List<DataPoint> points = new ArrayList<>();
        List<String> listnomevent = new ArrayList<String>();
        List<Integer> listcapacite = new ArrayList<Integer>();

        List<InternalService> clients = internalService.retrieveAllIService();
        for (int i = 0; i < clients.size(); i++) {
            listnomevent.add(clients.get(i).getName());
        }
        for (int i = 0; i < clients.size(); i++) {
            listcapacite.add(clients.get(i).getCapacite());
        }

        for (int i = 0; i < clients.size(); i++) {
            points.add(new DataPoint(Double.valueOf(listcapacite.get(i))));
        }
        KMeans kMeans = new KMeans(3, points, new RandomInitialiser());

        return kMeans;

    }
    @GetMapping("/eventpargroupeKmeans")
    @ResponseBody

    public Map<String, String> EventparGroupe() {

        Map<String, String> hash_map = new HashMap<>();

        List<Cluster> listOfClusters = kmeans().getClusters();
        List<String> listname = new ArrayList<String>();

        for (int i = 0; i < kmeans().k; i++) {
            int y = i + 1;
            String groupe = "Groupe" + y;

            Cluster cluster = listOfClusters.get(i);
            Set<DataPoint> SetOfPoints = cluster.getDataPoints();
            List<DataPoint> ListOfPoints = new ArrayList<>(SetOfPoints);
            int n = ListOfPoints.size();
            for (int j = 0; j < n; j++) {
                Double score = ListOfPoints.get(j).getComponents().get(0);
                String name = internalService.findInternalServiceByCapacite(score.intValue()).getName();
                hash_map.put(name, groupe);

            }

        }

        return hash_map;
    }
  @GetMapping("/eventidpargroupeKmeans")
    @ResponseBody
    public Map<Long, String> EventbyidPargroupe() {

        Map<Long, String> hash_map = new HashMap<>();

      List<Cluster> listOfClusters = kmeans().getClusters();
      List<String> listname = new ArrayList<String>();

        for (int i = 0; i < kmeans().k; i++) {
            int y = i + 1;
            String groupe = "Groupe" + y;

            Cluster cluster = listOfClusters.get(i);
            Set<DataPoint> SetOfPoints = cluster.getDataPoints();
            List<DataPoint> ListOfPoints = new ArrayList<>(SetOfPoints);

            int n = ListOfPoints.size();

            for (int j = 0; j < n; j++) {
                Double score = ListOfPoints.get(j).getComponents().get(0);

                Long id = (long) internalService.findInternalServiceByCapacite(score.intValue()).getId();

                hash_map.put(id, groupe);

            }

        }

        return hash_map;
    }

}
