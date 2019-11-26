package videofutur.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "User_Account", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "USER_ACCOUNT_UK", columnNames =  "User_Name") })


public class UserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "User_Id", nullable = false)
	private Long userId;

	@NotBlank(message = "Le nom d'utilisateur est obligatoire")
	@Column(name = "User_Name", length = 36, nullable = false)
	private String userName;

	@NotBlank(message = "Le mot de passe est obligatoire")
	@Column(name = "Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;

	@Column(name = "Enabled", length = 1, nullable = false)
	private boolean enabled;

//	@NotBlank(message = "Le nom est obligatoire")
	@Column(name = "Last_Name", length = 36, nullable = true)
	private String lastName;

//	@NotBlank(message = "Le prénom est obligatoire")
	@Column(name = "_First_Name", length = 36, nullable = true)
	private String firstName;

//	@NotBlank(message = "L'email est obligatoire")
	@Column(length = 36, nullable = false)
	private String email;
	
//	@NotBlank(message = "L'adresse est obligatoire")
	private String adress;
	
	
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	@OneToOne(cascade = CascadeType.ALL)
	private Cart cart;

	@OneToMany
	private List <Order> orders;

	private String fullName;

	// standard constructors / setters / getters / toString
}