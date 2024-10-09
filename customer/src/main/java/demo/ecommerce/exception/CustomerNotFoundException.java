package demo.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class CustomerNotFoundException extends RuntimeException {
    private final String msg;
}
