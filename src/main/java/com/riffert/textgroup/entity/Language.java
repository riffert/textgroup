package com.riffert.textgroup.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Languages generated by hbm2java
 */

@Entity
//@Table(name = "language", catalog = "thedatabase")
@Table(name = "language")
public class Language implements java.io.Serializable
{
		private Integer pk;
		private String alpha3B;
		private String alpha3T;
		private String alpha2;
		private String english;
		private String french;
	
		public Language() {}
	
		public Language(String alpha3B, String alpha3T, String alpha2, String english, String french) {
			this.alpha3B = alpha3B;
			this.alpha3T = alpha3T;
			this.alpha2 = alpha2;
			this.english = english;
			this.french = french;
		}
	
		@Id
		@GeneratedValue(strategy = IDENTITY)
	
		@Column(name = "pk", unique = true, nullable = false)
		public Integer getPk() {
			return this.pk;
		}
	
		public void setPk(Integer pk) {
			this.pk = pk;
		}
	
		@Column(name = "alpha3b", nullable = false, length = 50)
		public String getAlpha3B() {
			return this.alpha3B;
		}
	
		public void setAlpha3B(String alpha3B) {
			this.alpha3B = alpha3B;
		}
	
		@Column(name = "alpha3t", nullable = false, length = 50)
		public String getAlpha3T() {
			return this.alpha3T;
		}
	
		public void setAlpha3T(String alpha3T) {
			this.alpha3T = alpha3T;
		}
	
		@Column(name = "alpha2", nullable = false, length = 50)
		public String getAlpha2() {
			return this.alpha2;
		}
	
		public void setAlpha2(String alpha2) {
			this.alpha2 = alpha2;
		}
	
		@Column(name = "english", nullable = false, length = 50)
		public String getEnglish() {
			return this.english;
		}
	
		public void setEnglish(String english) {
			this.english = english;
		}
	
		@Column(name = "french", nullable = false, length = 50)
		public String getFrench() {
			return this.french;
		}
	
		public void setFrench(String french) {
			this.french = french;
		}
}
