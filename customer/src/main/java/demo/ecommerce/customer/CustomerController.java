package demo.ecommerce.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request) {
        customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.existsById(customerId));
    }


    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteById(@PathVariable("customer-id") String customerId) {
        customerService.deleteBuId(customerId);
        return ResponseEntity.accepted().build();
    }

}
