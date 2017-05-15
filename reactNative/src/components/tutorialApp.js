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
	Button
} from 'react-native';


//	Redux
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as counterActions from '../actions/counterActions';

class TutorialApp extends Component {
	constructor(props) {
		super(props);
	}

	static navigationOptions = {
		titile: "Welcome"
	};

	render() {
		const { counter, actions } = this.props;
		console.log("ASD");
		return (
			<View
				style={styles.container}
			>
				<Text style={styles.welcome}>
					Welcome to React Native! ASD
					</Text>
				<Text style={{ fontSize: 20, color: "#fff" }}>
					{counter.count}
				</Text>

				<View style={styles.horiFlex}>
					<Button
						title="+"
						color="#fff"
						onPress={actions.increment}
					/>
					<Button
						title="-"
						color="#fff"
						onPress={
							actions.decrement
						}
					/>
				</View>
			</View>
		);
	}
}



const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'center',
		alignItems: 'center',
		backgroundColor: '#222',
	},
	horiFlex: {
		justifyContent: "center",
		flexDirection: "row",
	},
	welcome: {
		fontSize: 20,
		textAlign: 'center',
		margin: 10,
		color: "#fff"
	},
	instructions: {
		textAlign: 'center',
		color: '#9f9f9f',
		marginBottom: 5,
	},
});

export default connect(state => ({
	counter: state.counter
}), (dispatch) => ({
	actions: bindActionCreators(counterActions, dispatch)
})
)(TutorialApp);



