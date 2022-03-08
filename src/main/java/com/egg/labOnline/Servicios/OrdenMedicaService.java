package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Foto;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.FotoRepository;
import com.egg.labOnline.Repositorios.ObraSocialRepository;
import com.egg.labOnline.Repositorios.OrdenMedicaRepository;
import com.egg.labOnline.Repositorios.UsuarioRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrdenMedicaService {

	@Autowired
	private OrdenMedicaRepository ordenMedicaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ObraSocialRepository obraSocialRepository;

	@Autowired
	private FotoService fService;

	public OrdenMedicaService() {
	}

	@Transactional
	public void create(List<Analisis> analisis, MultipartFile foto, Date fechaOrden, Usuario usuario,
			ObraSocial obrasocial) throws ErrorServicio {

		OrdenMedica nueva = new OrdenMedica();

		nueva.setAnalisis(analisis);
		nueva.setObraSocial(obrasocial);
		nueva.setUsuario(usuario);
		nueva.setFechaOrden(fechaOrden);

		Foto imagen = fService.crear(foto);
		nueva.setFoto(imagen);

		ordenMedicaRepository.save(nueva);
	}

	@Transactional
	public List<OrdenMedica> showAll() {
		return ordenMedicaRepository.findAll();
	}

	@Transactional
	public void edit(String id, List<Analisis> analisis, MultipartFile foto, Date fechaOrden, Usuario usuario,
			ObraSocial obrasocial) throws ErrorServicio {

		OrdenMedica edit = ordenMedicaRepository.findById(id).get();

		if (edit == null) {
			throw new ErrorServicio("No se encontro una orden medica con ese id");
		}

		if (analisis != null) {
			edit.setAnalisis(analisis);
		}

		if (obrasocial != null) {
			edit.setObraSocial(obrasocial);
		}

		if (usuario != null) {
			edit.setUsuario(usuario);
		}

		if (fechaOrden != null) {
			edit.setFechaOrden(fechaOrden);
		}

		if (foto != null) {
			Foto imagen = fService.editar(edit.getFoto().getId(), foto);
			edit.setFoto(imagen);
		} else {
			edit.setFoto(edit.getFoto());
		}

		ordenMedicaRepository.save(edit);
	}

	@Transactional
	public void delete(String id) {
		ordenMedicaRepository.deleteById(id);
	}

	@Transactional
	public OrdenMedica findById(String id) {
		return ordenMedicaRepository.getById(id);
	}

	@Transactional
	public List<OrdenMedica> SearchByUser(String id) {
		Usuario usuario = usuarioRepository.getById(id);
		return ordenMedicaRepository.SearchByUser(usuario);
	}

	@Transactional
	public List<OrdenMedica> SearchByOs(String id) {
		ObraSocial obraSocial = obraSocialRepository.getById(id);
		return ordenMedicaRepository.SearchByOs(obraSocial);
	}

	@Transactional
	public List<OrdenMedica> SearchByDate(Date fechaOrden) {
		return ordenMedicaRepository.SearchByDate(fechaOrden);
	}

	public void validate(List<Analisis> analisis, MultipartFile foto, Date fechaOrden, Usuario usuario,
			ObraSocial obrasocial) throws ErrorServicio {
		if (analisis.isEmpty() == true || analisis == null) {
			throw new ErrorServicio("Analisis vacio");
		}
		if (foto == null) {
			throw new ErrorServicio("Sin foto cargada");
		}
		if (fechaOrden == null) {
			throw new ErrorServicio("Sin fecha cargada");
		}
		if (usuario == null) {
			throw new ErrorServicio("Usuario vacio");
		}
		if (obrasocial == null) {
			throw new ErrorServicio("Obra social vacia");
		}
	}

}
