package fr.arty.seventhsea.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.arty.seventhsea.type.RaiseType;
import fr.arty.seventhsea.util.CalculationUtils;

/**
 * Represente l'ensemble des dés lancés
 * 
 * @author Arty
 *
 */
public class DicesSet {

	/** valeurs des dés triès par valeurs. */
	private Map<Integer, Integer> dicesMap = new HashMap<>();

	/** prévoir une évo pour le rang 3. */
	private RaiseType type = RaiseType.BASIC;

	/**
	 * Construteur par tableau.
	 * 
	 * @param numbers
	 *            Tableau contenant les valeurs des dés.
	 */
	public DicesSet(Integer[] numbers) {
		this.initialize();
		this.distribute(Arrays.asList(numbers));
	}

	/**
	 * Construteur par liste.
	 * 
	 * @param numbers
	 *            Tableau contenant les valeurs des dés.
	 */
	public DicesSet(List<Integer> numbers) {
		this.initialize();
		this.distribute(numbers);
	}

	/**
	 * Initialize la liste des dés.
	 */
	private void initialize() {
		for (int i = 1; i <= 10; i++) {
			dicesMap.put(i, 0);
		}
	}

	/**
	 * repartie les valeurs des dés dans la map dicesMap.
	 * 
	 * @param numbers
	 */
	private void distribute(List<Integer> numbers) {
		for (int i = 0; i < numbers.size(); i++) {
			this.dicesMap.put(numbers.get(i), dicesMap.get(numbers.get(i)) + 1);
		}
	}

	/**
	 * Supprime de la liste des dés l'ensemble des dés utilisés dans une mise.
	 * 
	 * @param raise
	 *            une mise à prendre en compte.
	 */
	public void removeRaiseDices(Raise raise) {
		for (Integer dice : raise.getDices()) {
			this.dicesMap.put(dice, this.dicesMap.get(dice) - 1);
		}
	}

	/**
	 * Retourne la liste des dés qui n'ont pas encore été utilisés.
	 * 
	 * @return la liste des dés non utilisés ou une liste vide si l'ensemble des dés
	 *         ont été utilisés.
	 */
	public List<Integer> dicesList() {
		List<Integer> dicesList = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			int nb = dicesMap.get(i);
			for (int j = 1; j <= nb; j++) {
				dicesList.add(i);
			}
		}
		return dicesList;
	}

	/**
	 * Somme des valeurs des dés.
	 * 
	 * @return Somme des valeurs des dés. 0 s'il y a aucun dés.
	 */
	public int total() {
		return this.dicesList().stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Moyenne des dés "utile", c'est à dire aprés avoir retirer le plus grand
	 * nombre de dés representant le module de la division de la somme des dés
	 * présent par le seuil pour valider une mise.
	 * 
	 * @return une moyenne comprise entre 0 et 10. 0 si aucun dés n'est présent.
	 */
	public BigDecimal usufulDicesAverage() {
		// Le but est de caluler une moyenne "utile"
		// C'est à dire qu'on essaye d'écarter le plus grand nombre de dés qui
		// potentiellement n'entreront pas dans les mises.

		// Pour se faire, on détermine les points en trop qui est le modulo de la somme
		// des dés par le seuil d'une mise.
		// De ce "reste" on retire le plus grand nombre de dés possible afin d'avoir la
		// sommes la plus proche avec plus petit nombre de dés "utiles"
		// On fait une moyenne à partir de là.

		int total = this.total();
		int useless = total % type.threshold();
		List<Integer> diceList = this.dicesList();
		if (useless > 0 && diceList.size() > 0) {
			while (diceList.size() > 0 && diceList.get(0) <= useless) {
				useless = useless - diceList.get(0);
				diceList.remove(0);
			}

		}
		if (diceList.size() == 0) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(diceList.stream().mapToInt(Integer::intValue).sum())
				.divide(new BigDecimal(diceList.size()), 4, BigDecimal.ROUND_UP);
	}

	/**
	 * La liste des n-tuples de taille données possibles à partir des dés présents
	 * ayant un seuil superieur ou égale au seuil du type de mise. La liste est
	 * ordonnée du plus petit total au plus grand.
	 * 
	 * Pour un seuil de 10 (mise basique) et les dés suivants 1 3 5 5 5 7 9 cela
	 * retournera (1, 9) (3, 7) (3, 9) (5, 5) (5, 5) (5, 7) (5, 9) ...
	 * 
	 * @param depth
	 *            taille des tuples. (ou combiens de dés il faut pour faire une
	 *            combinaison)
	 * @return La liste des n-tuples, une liste vide sinon.
	 */
	public List<Raise> combinations(int depth) {
		List<Integer> dicelist = this.dicesList();
		List<Integer> indexes = new ArrayList<>(depth);
		List<Raise> combinations = new ArrayList<>();

		// initialisation des indexes.
		// Selon la taille des tuples.
		// pour éviter les doublons, cela va de 0 pour le premier index à N pour le
		// nième indexe.
		for (int i = 0; i < depth; i++) {
			indexes.add(i);
		}

		boolean finish = false;
		// Tant qu'il y a suffisements de dés pour faire un tuple et qu'on a pas
		// parcouru l'ensemble du tableau.
		while (!finish && depth <= dicelist.size()) {
			// On recupère la prochaine commbinaison.
			Raise bet = nextCombination(0, depth, indexes, dicelist, null);
			// Seul les combinaisons qui présentent des dés sont valides.
			// un zéro signifie qu'il n'y a pas de dés.
			if (bet.total() > 0) {
				// seul les combinaisons avec un total superieur au seuil sont valides.
				if (bet.total() >= type.threshold()) {
					combinations.add(bet);
				}
				// Tant qu'on a pas parcouru l'ensemble du tableau ...
				if (indexes.get(0) <= (dicelist.size() - depth)) {
					// Si un index n est arrivé en bout de parcour, alors on incremente :
					// Exemple : un tableau de 4 valeurs
					for (int i = 1; i < depth; i++) {
						if (indexes.get(i) > (dicelist.size() - (depth - i))) {
							indexes.remove(i);
							indexes.add(i, indexes.get(i - 1) + 1);
						}
					}
				} else {
					finish = true;
				}
			} else {
				finish = true;
			}
		}

		// On veut la liste ordonnée du plus petit au plus grand !
		return CalculationUtils.sortRaises(combinations);
	}

	/**
	 * recherche la prochaine combinaison de dés !
	 * 
	 * @param depth
	 *            Prondeur (taille) du tuple.
	 * @param maxDepht
	 *            profondeur maximale.
	 * @param indexes
	 *            indexes.
	 * @param dicelist
	 *            liste des dés disponibles.
	 * @param bet
	 *            mise !
	 * @return La prochaine mise.
	 */
	public Raise nextCombination(int depth, int maxDepth, List<Integer> indexes, List<Integer> dicelist, Raise bet) {
		if (bet == null) {
			bet = new Raise();
		}
		// On enrichie le tuple de la mise d'autant de dés que la mise le permet et on
		// met les indexes à jours.
		if (depth < (maxDepth - 1)) {
			int indice = indexes.get(depth);
			bet.getDices().add(dicelist.get(indice));
			bet = nextCombination(depth + 1, maxDepth, indexes, dicelist, bet);
			if (indexes.get(depth + 1) > (dicelist.size() - (maxDepth - (depth + 1)))) {
				indexes.remove(depth);
				indexes.add(depth, indice + 1);
			}
		} else {
			Integer indice = indexes.get(depth);
			bet.getDices().add(dicelist.get(indice));
			indexes.remove(indice);
			indexes.add(indice + 1);
		}
		return bet;
	}

	/**
	 * Sommes des valeurs des dés présents.
	 * 
	 * @return la sommes des dés.
	 */
	public int dicesSum() {
		return dicesMap.values().stream().mapToInt(Integer::intValue).sum();
	}

	@Override
	public String toString() {
		List<Integer> list = this.dicesList();
		return String.format("[%s]",
				list.size() > 0 ? list.stream().sorted().map(i -> i.toString()).collect(Collectors.joining(", ")) : "");
	}

	// getters & setters 
	
	public Map<Integer, Integer> getDicesMap() {
		return dicesMap;
	}

	public void setDicesMap(Map<Integer, Integer> dicesMap) {
		this.dicesMap = dicesMap;
	}

	public RaiseType getType() {
		return type;
	}

	public void setType(RaiseType type) {
		this.type = type;
	}
}
