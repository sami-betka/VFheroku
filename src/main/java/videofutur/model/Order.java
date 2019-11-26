package videofutur.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class Order {
     
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     
    @ManyToOne
    private UserAccount user;
    
    private LocalDate date;
    
    private BigDecimal amount;
    
    private Delivery delivery;
    
//    private List<CartProduct> products;
     
    // standard constructors / setters / getters / toString
}