package fr.arty.seventhsea.type;

/**
 * Type de mises : 
 * Basic : 10 suffit à valider une mise
 * Evolved : Rand trois 15 pour valider 2 mises.
 * @author Arty
 *
 */
public enum RaiseType {
	/** 10 pour valider 1 mise. */
	BASIC(10),
	/** 15 pour valider 2 mises. */
	EVOLVED(15);
	
	/** Seuil minima pour valider une mise/ */
	int threshold;
	
	/**
	 * Constructeur par 
	 * @param seuil
	 */
	RaiseType(int seuil) {
		this.threshold = seuil; 
	}
	
	/**
	 * Seuil minimum pour valider la mise.
	 * @return Seuil minimum pour valider la mise.
	 */
	public int threshold() {
		return threshold;
	}

}
