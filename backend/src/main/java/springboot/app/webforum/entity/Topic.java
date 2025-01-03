package springboot.app.webforum.entity;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the topics database table.
 * 
 */
@Entity
@Table(name="topics")
public class Topic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="topic_name")
	private String topicName;

	public Topic() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}