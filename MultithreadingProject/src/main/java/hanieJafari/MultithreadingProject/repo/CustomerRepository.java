package hanieJafari.MultithreadingProject.repo;

import hanieJafari.MultithreadingProject.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}