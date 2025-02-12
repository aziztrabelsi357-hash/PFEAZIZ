package com.example.PfaBack;
import com.example.PfaBack.models.Product;
import com.example.PfaBack.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class PfaBackApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;  // Inject the repository

	public static void main(String[] args) {
		SpringApplication.run(PfaBackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Insert a product when the application starts
		Product product = new Product();
		product.setName("Rabies Vaccine");
		product.setDiseaseId("rabies");
		product.setDescription("Vaccine for rabies");

		productRepository.save(product);  // Save the product into the 'products' collection
		System.out.println("Product added: " + product.getName());
	}
}


