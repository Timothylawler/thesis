
import React, { Component } from 'react';
import {
	Text,
} from 'react-native';

//  style
import textStyle from '../styles/text';

//	Redux
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

//	Redux actions
import * as GeoActions from '../actions/geoActions';

var watchID = null;

/**
 * Class GeoLocation
 * Gets the device's position from the navigator.geolocation which uses MDN's geolocation
 * 	https://facebook.github.io/react-native/docs/geolocation.html
 * Puts up a watch for new positions and updates to the redux state on new data
 * outputs a single <Text> with either lat+long or "N/A"
 */
class GeoLocation extends Component {

	componentDidMount(){
		let self = this;
		//	Start geo location and add watcher
		navigator.geolocation.getCurrentPosition((position) => {
				this.props.actions.setPosition(position.coords);
			}, function(error){

			}, 
			{enableHighAccuracy: true, timeout: 2000, maximumAge: 1000}
		);
		//	Put a watch on new positions
		watchID = navigator.geolocation.watchPosition((position) => {
			this.props.actions.setPosition(position.coords);
		});
	}

	componentWillUnmount(){
		if(watchID != null){
			navigator.geolocation.clearWatch(watchID);
		}
	}

	render() {
		return (
			<Text style={textStyle.baseText}>
				{
					/*	Check so that we actually have a value to display	*/
					Object.keys(this.props.position).length > 0 ?
					"Lat: "+ this.props.position.position.latitude.toFixed(5) +", Long: " + this.props.position.position.longitude.toFixed(5)
					: "N/A"
				}
			</Text>
		);
	}
}

export default connect((state) => ({
	position: state.GeoReducer
}), (dispatch) => ({
	actions: bindActionCreators(GeoActions, dispatch)
})
)(GeoLocation);



