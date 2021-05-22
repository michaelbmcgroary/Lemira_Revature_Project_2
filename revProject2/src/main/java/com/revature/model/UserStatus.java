package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="glb_user_status")
public class UserStatus {


		@Id
		@Column(name = "user_status_id")
		private int statusID;
		
		@Column(name = "user_status", length=10)
		private String status;
		
		public UserStatus() {
			super();
		}

		public UserStatus(int statusID) {
			super();
			this.statusID = statusID;
			switch(statusID) {
				case 1: status = "Active";
					break;
				case 2: status = "Banned";
					break;
				default: status = "Active";
					this.statusID = 1;
					break;
			}
		}

		public UserStatus(String status) {
			this.status = status;
			switch(status) {
				case "Active": statusID = 1;
					break;
				case "Banned": statusID = 2;
					break;
				default: 
					statusID = 1;
					this.status = "Active";
					break;
			}
		}

		public int getStatusID() {
			return statusID;
		}

		public void setStatusID(int statusID) {
			this.statusID = statusID;
			setStatus(this.statusID);
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(int statusID) {
			switch(statusID) {
				case 1: status = "Active";
					break;
				case 2: status = "Banned";
					break;
				default: status = "Active";
					this.statusID = 1;
					break;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((status == null) ? 0 : status.hashCode());
			result = prime * result + statusID;
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
			UserStatus other = (UserStatus) obj;
			if (status == null) {
				if (other.status != null)
					return false;
			} else if (!status.equals(other.status))
				return false;
			if (statusID != other.statusID)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "UserStatus [statusID=" + statusID + ", status=" + status + "]";
		}

		
		
		
		

}
