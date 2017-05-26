/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
	Text,
	View,
	StyleSheet,
	Button,
	NativeModules,
	NetInfo,
	Platform,
	DeviceEventEmitter
} from 'react-native';


//	Components
import DefaultText from '../components/defaultText';
import GeoLocation from '../components/geoLocation';

//	Module
import HardwareProvider from '../modules/hardwareProvider';

//	Redux
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as CarrierActions from '../actions/carrierActions';
import * as NetworkActions from '../actions/networkInfoActions';
import * as PhoneStateActions from '../actions/phoneStateActions';
import * as TextMessageActions from '../actions/textMessageActions';

//	Router
import { Actions } from 'react-native-router-flux';


class HardwareScene extends Component {
	
	componentDidMount(){
		console.log("Hardware mount");
		this.getCarrierData();
		this.getNetworkInfo();
		this.listenForPhoneCall();
		this.listenForTextMessages();
	}
	//	Call native component to get information of the cell carrier
	getCarrierData(){
		let self = this;
		//	IOS
		if(Platform.OS === 'ios'){
			var carrierModule = NativeModules.CarrierInfo;
			carrierModule.getCarrierInfo(function(res){
				//	Ugly solution to fixing xCode array. 
				//	Works right now. i mean. why fix something that works amirite?
				//	TODO: Fix this...
				var carrier = {
					name: res[1].name,
					countryCode: res[2].countryCode
				}
				console.log("CARRIER: ", res);
				self.props.actions.setCarrier(carrier);
			});
		}
		else if(Platform.OS === "android"){
			HardwareProvider.getCarrier(function(res){
				self.props.actions.setCarrier(res);
			});
		}
	}

	//	Call NetInfo to get information of the network connection type
	getNetworkInfo(){
		NetInfo.addEventListener(
			'netInfoListener',
			this.handleConnectionChange.bind(this)
		);
	}

	//	Call the native componen PhoneStateListener to start listening for changes in the call state
	listenForPhoneCall(){
		//	IOS
		if(Platform.OS === 'ios'){
			var phoneStateManager = NativeModules.PhoneStateListener;
			let self = this;
			//	Call start listening and send handlePhoneStateChange as a callback method
			phoneStateManager.startListening(this.handlePhoneStateChange.bind(this));
		}
		else if(Platform.OS === "android"){
			HardwareProvider.listenOnPhone();
			//	Start phoneListener
			DeviceEventEmitter.addListener("PHONE_STATE", this.handlePhoneStateChangeAndroid.bind(this));
		}
	}

	listenForTextMessages(){
		//	Can only listen for text messages on android
		if(Platform.OS === "android"){

			HardwareProvider.listenOnText();
			DeviceEventEmitter.addListener("TEXT_MESSAGE", this.handleTextMessageReceived.bind(this));
		}
	}

	/**
	 * Event responder for when event: PHONE_STATE is emitted
	 * 
	 * @param {*} event 
	 */
	handlePhoneStateChangeAndroid(event){
		//console.log("PhoneState: ", event);
		if(event.PHONESTATE != undefined){
			this.props.actions.setPhoneState(event.PHONESTATE);
		}
	}

	handlePhoneStateChange(phoneState){

		this.props.actions.setPhoneState(phoneState);
		//	Call to listen for phone states again since native callbacks only allow one callback per callback
		this.listenForPhoneCall();
	}

	handleTextMessageReceived(event){

		if(event.TEXTMESSAGE != undefined){
			this.props.actions.setTextMessage(event.TEXTMESSAGE);
		}
	}

	//	Callback to when network connection type is changed
	handleConnectionChange(connection){

		this.props.actions.setNetworkInfo(connection);
	}

	// Clean up service
	componentWillUnmount(){
		NetInfo.removeEventListener('netInfoListener');
		DeviceEventEmitter.removeListener("PHONE_STATE");
		DeviceEventEmitter.removeListener("TEXT_MESSAGE");
		//	Cleanup hardwareProvider aswell
		if(HardwareProvider != undefined){
			HardwareProvider.unmount();
		}
	}

	componentWillReceiveProps(props){
		/*	Carrier	*/

		if(props.Carrier != undefined){
			this.props.actions.setCarrier(props.Carrier);
		}
	}

	render() {

		const { counter, actions } = this.props;
		return (
			<View style={styles.container}>
				<View style={styles.wrapper}>

					<View style={styles.horiFlex}>
						{/* GPS DATA */}
						<DefaultText text="GPS data: " />
						<GeoLocation />
					</View>

					<View style={styles.horiFlex}>
						{/* Carrier data */}
						<DefaultText text="Carrier data: " />
						<DefaultText text={ 
							this.props.Carrier != undefined ?
							/* This could be improved to only have this.props.Carrier.name and .countryCode 
									Has to be fixed in xCode then */
							this.props.Carrier.name + ", " + this.props.Carrier.countryCode
							: "N/A"
						}  />
					</View>

					<View style={styles.horiFlex}>
						{/* Network DATA */}
						<DefaultText text="Network information: " />
						<DefaultText text={
							this.props.Network != undefined?
							this.props.Network.network
							: "N/A"
						} />
					</View>

					<View style={styles.horiFlex}>
						{/* Phone state */}
						<DefaultText text="Phone state: " />
						<DefaultText text={
							this.props.PhoneState.phoneState != undefined?
							this.props.PhoneState.phoneState
							: "idle"
						} />
					</View>

					<View style={styles.horiFlex}>
						{/* Text message */}
						<DefaultText text="Text message: " />
						<DefaultText text={
							this.props.Message.message != undefined?
							this.props.Message.message
							: "N/A"
						}/>
					</View>

					<View style={styles.horiFlex}>
						<Button
							title="Long list"
							onPress={Actions.LongListScene}
						/>

						<Button
							title="Contacts"
							onPress={Actions.ContactsScene}
						/>
					</View>

					<View style={styles.horiFlex}>
						<Button
							title="Camera Photo"
							onPress={Actions.Camera_PhotoScene}
						/>

						<Button
							title="Camera Record"
							onPress={Actions.Camera_RecordScene}
						/>
					</View>
				</View>
			</View>
		);
	}
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		marginTop: 64,
		backgroundColor: '#fff',
		flexDirection: 'column',
		flexWrap: 'wrap',
	},
	wrapper: {
		padding: 12,
	},
	horiFlex: {
		flexDirection: "row",
		margin: 4
	},
});

const mapState = (state) => {
	//	Never ever remove the capital c from carrier. Have no idea why rnrf complains but it sure does
	return {
		Carrier: state.CarrierReducer.carrier,
		Network: state.NetworkInfoReducer,
		PhoneState: state.PhoneStateReducer,
		Message: state.TextMessageReducer
	}
};

/*	Dont think i need this now */
const mapRoutes = ({routes}) => {
	return {
		routes
	}
}

const mapActions = (dispatch) => {
	return {
		actions: bindActionCreators({
			...CarrierActions, 
			...NetworkActions,
			...PhoneStateActions,
			...TextMessageActions
		}, dispatch)
	}
}

export default connect(
	mapState,
	mapActions,
)(HardwareScene);





