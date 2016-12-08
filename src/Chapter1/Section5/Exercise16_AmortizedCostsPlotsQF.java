package Chapter1.Section5;

import Util.GraphPanel;
import edu.princeton.cs.algs4.StdRandom;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene on 08/12/16.
 */
public class Exercise16_AmortizedCostsPlotsQF {

    private class QuickFind {

        int[] id;
        int count;

        //Used for plotting amortized costs
        int operation;
        int currentCost;
        int totalCost;
        List<Integer> total;

        public QuickFind(int n) {
            id = new int[n];
            count = n;

            operation = 1;
            currentCost = 0;
            totalCost = 0;
            total = new ArrayList<>();

            for(int i=0; i < id.length; i++) {
                id[i] = i;
            }
        }

        public int count() {
            return count;
        }

        //O(1)
        public int find(int site) {
            currentCost++;

            return id[site];
        }

        //O(1)
        public boolean connected(int site1, int site2) {
            boolean isConnected = find(site1) == find(site2);

            if(isConnected) {
                updateCostAnalysis();
            }

            return isConnected;
        }

        //O(n)
        public void union(int site1, int site2) {
            int leaderId1 = find(site1);
            int leaderId2 = find(site2);

            if(leaderId1 == leaderId2) {
                return;
            }

            for(int i=0; i < id.length; i++) {
                currentCost++; // 1 access for every site

                if(id[i] == leaderId1) {
                    id[i] = leaderId2;

                    currentCost++; // 1 access for every component merged
                }
            }

            count--;

            updateCostAnalysis();
        }

        private void updateCostAnalysis() {

            totalCost += currentCost;

            total.add(totalCost / operation);

            currentCost = 0;
            operation++;
        }
    }

    public static void main(String[] args) {

        int numberOfSites = 100;

        Exercise16_AmortizedCostsPlotsQF amortizedCostsPlots = new Exercise16_AmortizedCostsPlotsQF();
        QuickFind quickFind = amortizedCostsPlots.new QuickFind(numberOfSites);

        for(int i=0; i < 150; i++) {
            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            if(quickFind.connected(randomSite1, randomSite2)) {
                continue;
            }

            quickFind.union(randomSite1, randomSite2);
        }

        System.out.println("Components: " + quickFind.count);

        amortizedCostsPlots.draw(quickFind);
    }

    private void draw(QuickFind quickFind) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphPanel graphPanel = new GraphPanel("QuickFind", "Amortized Cost Plot",
                        "Number of Connections", "Number of Array Accesses", quickFind.total);
                graphPanel.createAndShowGui();
            }
        });
    }

}
