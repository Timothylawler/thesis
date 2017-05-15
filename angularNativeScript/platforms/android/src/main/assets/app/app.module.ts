import { NgModule, NO_ERRORS_SCHEMA } from "@angular/core";
import { NativeScriptModule } from "nativescript-angular/nativescript.module";
import { NativeScriptRouterModule } from "nativescript-angular/router";

import { Routes, NavigatableComponents } from "./app.routing";

import { AppComponent } from "./app.component";

@NgModule({
  declarations: [
		AppComponent,
		...NavigatableComponents
	],
  bootstrap: [AppComponent],
  imports: [
		NativeScriptModule,
		NativeScriptRouterModule,
    NativeScriptRouterModule.forRoot(Routes)
	],
  schemas: [NO_ERRORS_SCHEMA],
})
export class AppModule {}
