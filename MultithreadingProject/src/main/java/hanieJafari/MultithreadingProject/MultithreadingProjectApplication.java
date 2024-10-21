package hanieJafari.MultithreadingProject;

import hanieJafari.MultithreadingProject.service.CsvProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class MultithreadingProjectApplication implements CommandLineRunner {

	@Autowired
	private CsvProcessor csvProcessor;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MultithreadingProjectApplication.class);
		app.setHeadless(false);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Start the Swing GUI in a separate thread
		SwingUtilities.invokeLater(() -> new FileUploadApplication(csvProcessor));
	}
}