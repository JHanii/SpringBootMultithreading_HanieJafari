package hanieJafari.MultithreadingProject;

import hanieJafari.MultithreadingProject.service.CsvProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileUploadApplication {

    private CsvProcessor csvProcessor;

    public FileUploadApplication(CsvProcessor csvProcessor) {
        this.csvProcessor = csvProcessor;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("File Upload");
        JButton uploadButton = new JButton("Upload CSV File");
        JLabel label = new JLabel("Choose a CSV file to upload:");

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        csvProcessor.processCsvFile(selectedFile);
                        JOptionPane.showMessageDialog(frame, "File uploaded successfully: " + selectedFile.getName());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error uploading file: " + ex.getMessage());
                    }
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(null);
        label.setBounds(50, 30, 300, 30);
        uploadButton.setBounds(150, 100, 120, 30);
        frame.add(label);
        frame.add(uploadButton);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        CsvProcessor csvProcessor = new CsvProcessor();
        new FileUploadApplication(csvProcessor);
    }
}