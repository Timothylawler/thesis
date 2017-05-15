const initialState = {}

export default function PickedContactReducer(state = initialState, action) {
	switch (action.type) {
		case "SETPICKEDCONTACT":
			return Object.assign({}, state, {
				name: action.name
			});
			
		default:
			return state;
	}
};