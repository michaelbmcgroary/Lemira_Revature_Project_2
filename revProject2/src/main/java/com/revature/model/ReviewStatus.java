package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="glb_review_status")
public class ReviewStatus {


		@Id
		@Column(name = "review_completion_status_id")
		private int statusID;
		
		@Column(name = "review_completion_status", length=20)
		private String status;
		
		public ReviewStatus() {
			super();
		}

		public ReviewStatus(int statusID) {
			super();
			this.statusID = statusID;
			switch(statusID) {
				case 1: status = "Played";
					break;
				case 2: status = "Finished";
					break;
				case 3: status = "Completed";
					break;
				case 4: status = "Watched Playthrough";
					break;
				default: status = "Played";
					this.statusID = 1;
					break;
			}
		}

		public ReviewStatus(String status) {
			this.status = status;
			switch(status) {
				case "Played": statusID = 1;
					break;
				case "Finished": statusID = 2;
					break;
				case "Completed": statusID = 2;
					break;
				case "Watched Playthrough": statusID = 2;
					break;
				default: 
					statusID = 1;
					this.status = "Played";
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
			case 1: status = "Played";
				break;
			case 2: status = "Finished";
				break;
			case 3: status = "Completed";
				break;
			case 4: status = "Watched Playthrough";
				break;
			default: status = "Played";
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
			ReviewStatus other = (ReviewStatus) obj;
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
			return "ReviewStatus [statusID=" + statusID + ", status=" + status + "]";
		}

		
		
		
		
		
		

}
