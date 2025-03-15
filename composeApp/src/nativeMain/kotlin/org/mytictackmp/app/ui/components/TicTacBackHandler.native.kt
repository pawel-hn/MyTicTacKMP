package org.mytictackmp.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UIKit.navigationController
import platform.UIKit.navigationItem
import platform.darwin.NSObject

@Composable
actual fun TicTacBackHandler(onBack: () -> Unit) {
    val viewController = remember { getUIViewController() }

    val navigationController = viewController.navigationController
    println("navigationController: $navigationController")
    viewController.navigationController?.interactivePopGestureRecognizer?.enabled = true
    viewController.navigationItem.setHidesBackButton(false)

    DisposableEffect(viewController) {
        val delegate = object : NSObject(), UINavigationControllerDelegateProtocol {
            override fun navigationController(
                navigationController: UINavigationController,
                willShowViewController: UIViewController,
                animated: Boolean
            ) {
                if (willShowViewController != viewController) {
                    println("BACK ACTION DETECTED")
                    onBack()
                }
            }
        }

        val navigationController = viewController.navigationController
        navigationController?.delegate = delegate

        onDispose {
            if (navigationController?.delegate == delegate) {
                navigationController?.delegate = null
            }
        }
    }
}


fun getUIViewController(): UIViewController {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    return keyWindow?.rootViewController ?: error("No root ViewController found")
}