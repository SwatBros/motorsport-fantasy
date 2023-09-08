import { Player } from "./player";
import { Series } from "./series";

export interface League {
	name: string,
	owner: string,
	players: [
		Player
	],
	series: [Series]
}
