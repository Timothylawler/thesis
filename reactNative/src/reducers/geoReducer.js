const initialState = {}

export default function GeoReducer(state = initialState, action) {
	switch (action.type) {
		case "SETPOSITION":
			return Object.assign({}, state, {
				position: action.position
			});
			
		default:
			return state;
	}
};