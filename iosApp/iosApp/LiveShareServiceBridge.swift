import UIKit

@objc class LiveShareServiceBridge: NSObject {
    @objc static func shareSnapshotOfCurrentRootViewWithFileName(_ fileName: String, completion: @escaping (NSError?) -> Void) {
        DispatchQueue.main.async {
            guard let rootView = UIApplication.shared.keyWindow?.rootViewController?.view else {
                completion(NSError(domain: "LiveShareServiceBridge", code: 1, userInfo: [NSLocalizedDescriptionKey: "Root view unavailable"]))
                return
            }

            let controller = UIHostingController(rootView: rootView.snapshotable())
            let targetSize = controller.view.intrinsicContentSize
            controller.view.bounds = CGRect(origin: .zero, size: targetSize)
            controller.view.backgroundColor = .clear

            let renderer = UIGraphicsImageRenderer(size: targetSize)
            let image = renderer.image { _ in
                controller.view.drawHierarchy(in: controller.view.bounds, afterScreenUpdates: true)
            }

            do {
                try LiveShareService().share(image: image, fileName: fileName)
                completion(nil)
            } catch let error as NSError {
                completion(error)
            }
        }
    }
}


extension UIView {
    func snapshotable() -> some View {
        // Replace with your actual SwiftUI content if needed
        Color.clear.frame(width: 1, height: 1)
    }
}
