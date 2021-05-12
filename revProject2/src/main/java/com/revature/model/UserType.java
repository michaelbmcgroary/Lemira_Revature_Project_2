package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="glb_user_type")
public class UserType {


		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "user_type_id")
		private int typeID;
		
		@Column(name = "user_type", length=10)
		private String type;
		
		public UserType() {
			super();
		}

		public UserType(int typeID) {
			super();
			this.typeID = typeID;
			switch(typeID) {
				case 1: type = "Basic";
					break;
				case 2: type = "Moderator";
					break;
				default: type = "Basic";
					this.typeID = 1;
					break;
			}
		}

		public UserType(String type) {
			this.type = type;
			switch(type) {
				case "Basic": typeID = 1;
					break;
				case "Moderator": typeID = 2;
					break;
				default: 
					typeID = 1;
					this.type = "Basic";
					break;
			}
		}

		public int getTypeID() {
			return typeID;
		}

		public void setTypeID(int typeID) {
			this.typeID = typeID;
			setType(this.typeID);
		}

		public String getType() {
			return type;
		}

		public void setType(int typeID) {
			switch(typeID) {
				case 1: type = "Basic";
					break;
				case 2: type = "Moderator";
					break;
				default: type = "Basic";
					this.typeID = 1;
					break;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + typeID;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UserType other = (UserType) obj;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (typeID != other.typeID)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "UserType [typeID=" + typeID + ", type=" + type + "]";
		}

		
		
		
		

}
