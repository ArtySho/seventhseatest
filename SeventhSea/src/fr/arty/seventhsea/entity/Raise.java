package fr.arty.seventhsea.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represente une mise.
 * 
 * @author Arty
 *
 */
public class Raise {

	/** Liste des dés nécessaires pour réaliser la mise. */
	private List<Integer> dices = new ArrayList<>();

	/**
	 * Total des valeurs des dés de la mise.
	 * 
	 * @return Total des valeurs des dés de la mise
	 */
	public int total() {
		return dices.stream().mapToInt(Integer::intValue).sum();
	}

	@Override
	public String toString() {
		return String.format("[%s]",
				dices.size() > 0 ? dices.stream().sorted().map(i -> i.toString()).collect(Collectors.joining(", "))
						: "");
	}
	
	// getters & setters
	
	/**
	 * Getter.
	 * @return
	 */
	public List<Integer> getDices() {
		return dices;
	}

	/**
	 * Setter
	 * @param dices
	 */
	public void setDices(List<Integer> dices) {
		this.dices = dices;
	}

}
