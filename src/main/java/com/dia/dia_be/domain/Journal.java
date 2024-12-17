package com.dia.dia_be.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Journal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "INT UNSIGNED")
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String contents;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean complete;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "consulting_id")
	private Consulting consulting;

	@OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Script> script = new ArrayList<>();

	@OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<JournalProduct> journalProduct = new ArrayList<>();

	@OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<JournalKeyword> journalKeyword = new ArrayList<>();

	public Journal update(String contents) {
		this.contents = contents;
		return this;
	}

	public Journal update(boolean complete) {
		this.complete = complete;
		return this;
	}

	public Journal update(String contents, boolean complete) {
		this.contents = contents;
		this.complete = complete;
		return this;
	}

	protected void setConsulting(Consulting consulting) {
		this.consulting = consulting;
	}

}
