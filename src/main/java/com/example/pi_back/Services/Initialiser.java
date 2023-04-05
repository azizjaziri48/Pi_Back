package com.example.pi_back.Services;

import com.example.pi_back.Entities.DataPoint;

import java.util.List;

public interface Initialiser {
    List<DataPoint> createInitialCentroids(int k, List<DataPoint> points);
}
