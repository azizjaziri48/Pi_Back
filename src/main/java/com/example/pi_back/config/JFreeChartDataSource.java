package com.example.pi_back.config;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class JFreeChartDataSource {

    @Bean
    public JFreeChartDataSource jFreeChartDataSource() {
        return new JFreeChartDataSource();
    }



    public JFreeChart createPieChart(PieDataset dataset, int width, int height) {
        JFreeChart chart = ChartFactory.createPieChart("Pie Chart", dataset, true, true, false);

        chart.setBackgroundPaint(Color.white);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
    }
}
