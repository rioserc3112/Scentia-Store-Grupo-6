package com.store.controller;

import com.store.domain.Producto;
import com.store.domain.Usuario;
import com.store.service.FirebaseStorageService;
import com.store.service.ProductoService;
import jakarta.servlet.http.HttpSession;
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

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol());
    }

    @GetMapping("/listado")
    public String listado(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("producto", new Producto());
        return "/producto/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult result,
                          @RequestParam("imagenFile") MultipartFile imagenFile,
                          RedirectAttributes redirectAttributes,
                          Model model,
                          HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        if (result.hasErrors()) {
            var productos = productoService.getProductos(false);
            model.addAttribute("productos", productos);
            model.addAttribute("totalProductos", productos.size());
            return "/producto/listado";
        }

        try {
            productoService.save(producto);

            if (imagenFile != null && !imagenFile.isEmpty()) {
                String url = firebaseStorageService.uploadImage(
                        imagenFile,
                        "productos",
                        producto.getId().intValue()
                );

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
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

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
    public String modificar(@PathVariable("idProducto") Long idProducto,
                            Model model,
                            HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        return productoService.getProducto(idProducto)
                .map(p -> {
                    model.addAttribute("producto", p);
                    return "/producto/modifica";
                })
                .orElse("redirect:/producto/listado");
    }
}