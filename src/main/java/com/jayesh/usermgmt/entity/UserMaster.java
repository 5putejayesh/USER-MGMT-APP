package com.jayesh.usermgmt.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "USER_MASTER",uniqueConstraints = {
		@UniqueConstraint(name="uniqueEmail",columnNames = {
				"email"
		})
})
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fullName;
	private String email;
	private Long mobile;
	private String gender;
	private LocalDate dob;
	private Long ssn;
	private String password;
	private String accStatus;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDate createDate;
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDate updateDate;
	@Column(updatable = false)
	private String createdBy;
	@Column(insertable = false)
	private String updatedBy;

}
