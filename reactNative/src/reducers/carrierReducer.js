

const initialState = {}

export default function CarrierReducer(state = initialState, action) {
	switch (action.type) {
		case "SETCARRIER":
			//	Assign state the new object
			return Object.assign({}, state.carrier, {
				carrier: action.carrier
			});
			
		default:
			return state;
	}
};