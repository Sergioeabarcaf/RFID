package com.example.sdksamples;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import java.sql.*;

import java.util.List;

public class TagReportListenerImplementation implements TagReportListener {

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {

        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            System.out.print(" EPC: " + t.getEpc().toString());

            if (reader.getName() != null) {
                System.out.print(" Reader_name: " + reader.getName());
            } else {
                System.out.print(" Reader_ip: " + reader.getAddress());
            }

            if (t.isAntennaPortNumberPresent()) {
                System.out.print(" antenna: " + t.getAntennaPortNumber());
            }

            if (t.isFirstSeenTimePresent()) {
                System.out.print(" first: " + t.getFirstSeenTime().ToString());
            }

            if (t.isLastSeenTimePresent()) {
                System.out.print(" last: " + t.getLastSeenTime().ToString());
            }

            if (t.isSeenCountPresent()) {
                System.out.print(" count: " + t.getTagSeenCount());
            }

            if (t.isRfDopplerFrequencyPresent()) {
                System.out.print(" doppler: " + t.getRfDopplerFrequency());
            }

            if (t.isPeakRssiInDbmPresent()) {
                System.out.print(" peak_rssi: " + t.getPeakRssiInDbm());
            }

            if (t.isChannelInMhzPresent()) {
                System.out.print(" chan_MHz: " + t.getChannelInMhz());
            }

            if (t.isFastIdPresent()) {
                System.out.print("\n     fast_id: " + t.getTid().toHexString());

                System.out.print(" model: "
                        + t.getModelDetails().getModelName());

                System.out.print(" epcsize: "
                        + t.getModelDetails().getEpcSizeBits());

                System.out.print(" usermemsize: "
                        + t.getModelDetails().getUserMemorySizeBits());
            }

            try {
                // create a mysql database connection
                String myDriver = "org.gjt.mm.mysql.Driver";
                String myUrl = "jdbc:mysql://localhost/pruebaRfid";
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/pruebaRfid", "root", "proteinlab04");

                //Statement st = conn.createStatement();

                // note that i'm leaving "date_created" out of this insert statement
                String query = "";
                query = "INSERT INTO etapa1 (epc, date, señal) VALUES (?, ?, ?)";
                Statement stt = conn.prepareStatement(query);
                stt.setString(1, t.getEpc().toString());
                stt.setString(2, "jueves 19 enero 2017, 9:45");
                stt.setString(3, t.getPeakRssiInDbm());
                stt.execute(query);
                
                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }

            System.out.println("");
        }
    }
}
