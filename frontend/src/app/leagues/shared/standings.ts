import { Player } from "./player";

export interface Standings {
	[key: string]: [{player: string, series: string, points: number}]
}
