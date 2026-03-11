package com.store.controller;

import com.store.domain.Producto;
import com.store.service.FirebaseStorageService;
import com.store.service.ProductoService;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProductoService productoService;
    private final MessageSource messageSource;
    private final FirebaseStorageService firebaseStorageService;

    public ProductoController(ProductoService productoService, 
                              MessageSource messageSource, 
                              FirebaseStorageService firebaseStorageService) {
        this.productoService = productoService;
        this.messageSource = messageSource;
        this.firebaseStorageService = firebaseStorageService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        // Objeto para el formulario de agregar nuevo
        model.addAttribute("producto", new Producto());
        return "/producto/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto, 
                          BindingResult result,
                          @RequestParam("imagenFile") MultipartFile imagenFile, 
                          RedirectAttributes redirectAttributes,
                          Model model) {
        
        // 1. Validar errores de anotaciones (NotNull, Min, etc.)
        if (result.hasErrors()) {
            var productos = productoService.getProductos(false);
            model.addAttribute("productos", productos);
            return "/producto/listado";
        }

        try {
            // 2. Guardar primero para asegurar que tenemos un ID (necesario para el nombre de la imagen)
            productoService.save(producto);

            // 3. Si hay una imagen, subirla a Firebase
            if (imagenFile != null && !imagenFile.isEmpty()) {
                // Usamos el ID del producto recién guardado y la carpeta "productos"
                String url = firebaseStorageService.uploadImage(
                        imagenFile, 
                        "productos", 
                        producto.getId().intValue() // Convertimos Long a Integer para tu firma de método
                );
                
                // 4. Actualizar el producto con la URL de la imagen y guardar de nuevo
                producto.setImagenUrl(url);
                productoService.save(producto);
            }

            redirectAttributes.addFlashAttribute("todoOk", 
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el producto: " + e.getMessage());
        }

        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
public String eliminar(@RequestParam("idProducto") Long idProducto, 
                       RedirectAttributes redirectAttributes) {
    try {
        productoService.delete(idProducto);
        redirectAttributes.addFlashAttribute("todoOk", 
            messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "No se puede eliminar: tiene datos asociados.");
    }
    return "redirect:/producto/listado";
}

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable("idProducto") Long idProducto, Model model) {
        return productoService.getProducto(idProducto)
                .map(p -> {
                    model.addAttribute("producto", p);
                    return "/producto/modifica";
                })
                .orElse("redirect:/producto/listado");
    }
}