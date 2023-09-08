export interface Driver {
	name: string,
	points: number,
	trend: number,
	position: number,
	imgRef: string,
	value: number
	results: [
		{
			index: number,
			points: number,
			name: string,
		}
	]
}