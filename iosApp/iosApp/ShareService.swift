
import Foundation
import UIKit

protocol ShareService {
    func share(text: String)
    func share(image: UIImage, fileName: String) throws
}

enum ShareServiceShareImageError: Error {
    case shareImageImageDataUnavailable(fileName: String)
    case shareImageInsufficientStorage(fileName: String, cause: Error?)
    case shareImageStoragePermissionDenied(fileName: String, cause: Error?)
    case shareImageUnknownReason(fileName: String, cause: Error?)

    var errorDescription: String? {
        switch self {
        case .shareImageImageDataUnavailable(let fileName):
            return "Share image data not available for file name: \(fileName)"
        case .shareImageInsufficientStorage(let fileName, let cause):
            return "Share image error on file name: \(fileName), Cause: \(cause?.localizedDescription ?? "Unknown")"
        case .shareImageStoragePermissionDenied(let fileName, let cause):
            return "Share image error on file name: \(fileName), Cause: \(cause?.localizedDescription ?? "Unknown")"
        case .shareImageUnknownReason(let fileName, let cause):
            return "Share image error on file name: \(fileName), Cause: \(cause?.localizedDescription ?? "Unknown")"
        }
    }
}

final class LiveShareService: ShareService {
    func share(text: String) {
        guard var topViewController = UIApplication.shared.keyWindow?.rootViewController else {
            return
        }

        // Ensure we have the topmost presented view controller
        while let presentedViewController = topViewController.presentedViewController {
            topViewController = presentedViewController
        }

        let activityViewController = UIActivityViewController(activityItems: [text], applicationActivities: nil)
        topViewController.present(activityViewController, animated: true)
    }

    func share(image: UIImage, fileName: String) throws {
        guard var topViewController = UIApplication.shared.keyWindow?.rootViewController else {
            return
        }

        guard let imageData = image.pngData() else {
            throw ShareServiceShareImageError.shareImageImageDataUnavailable(fileName: fileName)
        }

        let imageFileName = "\(fileName).png"
        let uniqueFolder = NSUUID().uuidString
        let directoryUrl = FileManager.default.temporaryDirectory
        let uniqueDirectoryUrl = directoryUrl.appendingPathComponent(uniqueFolder)
        let uniqueImageFile = uniqueDirectoryUrl.appendingPathComponent(imageFileName)

        do {
            try FileManager.default.createDirectory(at: uniqueDirectoryUrl, withIntermediateDirectories: true, attributes: nil)

            try imageData.write(to: uniqueImageFile)

            // Ensure we have the topmost presented view controller
            while let presentedViewController = topViewController.presentedViewController {
                topViewController = presentedViewController
            }

            let activityViewController = UIActivityViewController(activityItems: [uniqueImageFile], applicationActivities: nil)

            activityViewController
                .completionWithItemsHandler = { (_, _, _, _) in
                    do {
                        try FileManager.default.removeItem(atPath: uniqueDirectoryUrl.path)
                    } catch {
                        debugPrint("failed to tidy up share image file")
                    }
                }

            topViewController.present(activityViewController, animated: true)
        } catch let error as NSError {
            switch error.code {
            case NSFileWriteNoPermissionError:
                throw ShareServiceShareImageError.shareImageStoragePermissionDenied(fileName: imageFileName, cause: error)
            case NSFileWriteOutOfSpaceError:
                throw ShareServiceShareImageError.shareImageInsufficientStorage(fileName: imageFileName, cause: error)
            default:
                throw ShareServiceShareImageError.shareImageUnknownReason(fileName: imageFileName, cause: error)
            }
        } catch {
            throw ShareServiceShareImageError.shareImageUnknownReason(fileName: imageFileName, cause: error)
        }
    }
}

