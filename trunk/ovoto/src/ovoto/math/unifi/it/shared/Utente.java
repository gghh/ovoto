package ovoto.math.unifi.it.shared;

import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Unindexed;



@Unindexed
public class Utente implements IsSerializable {

	public enum Status { VALIDO, DA_VERIFICARE, DISABILITATO };


	@Id String id; // identificativo unico dell'utente non e' la identity 
	private String identity;
	private String nome;
	private String cognome;
	private String titolo;
	private String affiliazione;
	private String indirizzo;
	private String email;
	private String url;
	private Date  data_creazione;
	private Date data_ultimo_accesso;
	private Date data_modifica;
	private Status status;
	private String hashed_code; //la password ma non la chiamiamo password

	public Utente() {};
	

	public void setId(String id) {
		this.id = id;	
	}
	
	
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getIdentity() {	
		return identity; 
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAffiliazione(String affiliazione) {
		this.affiliazione = affiliazione;
	}

	public String getAffiliazione() {
		return affiliazione;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setData_creazione(Date data_creazione) {
		this.data_creazione = data_creazione;
	}

	public Date getData_creazione() {
		return data_creazione;
	}

	public void setData_ultimo_accesso(Date data_ultimo_accesso) {
		this.data_ultimo_accesso = data_ultimo_accesso;
	}

	public Date getData_ultimo_accesso() {
		return data_ultimo_accesso;
	}

	public void setData_modifica(Date data_modifica) {
		this.data_modifica = data_modifica;
	}

	public Date getData_modifica() {
		return data_modifica;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public String getId() {
		return id;
	}


	public void setCode(String hashed_code) {
		this.hashed_code = hashed_code;
	}

	public String getCode() {
		return hashed_code;
	}
}

