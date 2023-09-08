export interface Player {
	name: string,
	points: number,
	money: number,
	contracts: [
		{
			driverName: string,
			points: number,
			value: number,
			trend: number,
			start: string,
		}
	]
}
